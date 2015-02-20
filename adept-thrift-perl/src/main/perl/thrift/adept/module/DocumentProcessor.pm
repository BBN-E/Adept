#
# Autogenerated by Thrift Compiler (0.9.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
require 5.6.0;
use strict;
use warnings;
use Thrift;

use thrift::adept::module::Types;
use thrift::adept::common::Types;

# HELPER FUNCTIONS AND STRUCTURES

package thrift::adept::module::DocumentProcessor_process_args;
use base qw(Class::Accessor);
thrift::adept::module::DocumentProcessor_process_args->mk_accessors( qw( document hltContentContainer ) );

sub new {
          my $classname = shift;
          my $self      = {};
          my $vals      = shift || {};
          $self->{document} = undef;
          $self->{hltContentContainer} = undef;
          if (UNIVERSAL::isa($vals,'HASH')) {
            if (defined $vals->{document}) {
              $self->{document} = $vals->{document};
            }
            if (defined $vals->{hltContentContainer}) {
              $self->{hltContentContainer} = $vals->{hltContentContainer};
            }
          }
          return bless ($self, $classname);
}

sub getName {
          return 'DocumentProcessor_process_args';
        }

sub read {
          my ($self, $input) = @_;
          my $xfer  = 0;
          my $fname;
          my $ftype = 0;
          my $fid   = 0;
          $xfer += $input->readStructBegin(\$fname);
          while (1) 
          {
            $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
            if ($ftype == TType::STOP) {
              last;
            }
            SWITCH: for($fid)
            {
              /^1$/ && do{              if ($ftype == TType::STRUCT) {
                $self->{document} = new thrift::adept::common::Document();
                $xfer += $self->{document}->read($input);
              } else {
                $xfer += $input->skip($ftype);
              }
              last; };
              /^2$/ && do{              if ($ftype == TType::STRUCT) {
                $self->{hltContentContainer} = new thrift::adept::common::HltContentContainer();
                $xfer += $self->{hltContentContainer}->read($input);
              } else {
                $xfer += $input->skip($ftype);
              }
              last; };
                $xfer += $input->skip($ftype);
            }
            $xfer += $input->readFieldEnd();
          }
          $xfer += $input->readStructEnd();
          return $xfer;
        }

sub write {
          my ($self, $output) = @_;
          my $xfer   = 0;
          $xfer += $output->writeStructBegin('DocumentProcessor_process_args');
          if (defined $self->{document}) {
            $xfer += $output->writeFieldBegin('document', TType::STRUCT, 1);
            $xfer += $self->{document}->write($output);
            $xfer += $output->writeFieldEnd();
          }
          if (defined $self->{hltContentContainer}) {
            $xfer += $output->writeFieldBegin('hltContentContainer', TType::STRUCT, 2);
            $xfer += $self->{hltContentContainer}->write($output);
            $xfer += $output->writeFieldEnd();
          }
          $xfer += $output->writeFieldStop();
          $xfer += $output->writeStructEnd();
          return $xfer;
        }

package thrift::adept::module::DocumentProcessor_process_result;
use base qw(Class::Accessor);
thrift::adept::module::DocumentProcessor_process_result->mk_accessors( qw( success ) );

sub new {
          my $classname = shift;
          my $self      = {};
          my $vals      = shift || {};
          $self->{success} = undef;
          if (UNIVERSAL::isa($vals,'HASH')) {
            if (defined $vals->{success}) {
              $self->{success} = $vals->{success};
            }
          }
          return bless ($self, $classname);
}

sub getName {
          return 'DocumentProcessor_process_result';
        }

sub read {
          my ($self, $input) = @_;
          my $xfer  = 0;
          my $fname;
          my $ftype = 0;
          my $fid   = 0;
          $xfer += $input->readStructBegin(\$fname);
          while (1) 
          {
            $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
            if ($ftype == TType::STOP) {
              last;
            }
            SWITCH: for($fid)
            {
              /^0$/ && do{              if ($ftype == TType::STRUCT) {
                $self->{success} = new thrift::adept::common::HltContentContainer();
                $xfer += $self->{success}->read($input);
              } else {
                $xfer += $input->skip($ftype);
              }
              last; };
                $xfer += $input->skip($ftype);
            }
            $xfer += $input->readFieldEnd();
          }
          $xfer += $input->readStructEnd();
          return $xfer;
        }

sub write {
          my ($self, $output) = @_;
          my $xfer   = 0;
          $xfer += $output->writeStructBegin('DocumentProcessor_process_result');
          if (defined $self->{success}) {
            $xfer += $output->writeFieldBegin('success', TType::STRUCT, 0);
            $xfer += $self->{success}->write($output);
            $xfer += $output->writeFieldEnd();
          }
          $xfer += $output->writeFieldStop();
          $xfer += $output->writeStructEnd();
          return $xfer;
        }

package thrift::adept::module::DocumentProcessor_processAsync_args;
use base qw(Class::Accessor);
thrift::adept::module::DocumentProcessor_processAsync_args->mk_accessors( qw( document hltContentContainer ) );

sub new {
          my $classname = shift;
          my $self      = {};
          my $vals      = shift || {};
          $self->{document} = undef;
          $self->{hltContentContainer} = undef;
          if (UNIVERSAL::isa($vals,'HASH')) {
            if (defined $vals->{document}) {
              $self->{document} = $vals->{document};
            }
            if (defined $vals->{hltContentContainer}) {
              $self->{hltContentContainer} = $vals->{hltContentContainer};
            }
          }
          return bless ($self, $classname);
}

sub getName {
          return 'DocumentProcessor_processAsync_args';
        }

sub read {
          my ($self, $input) = @_;
          my $xfer  = 0;
          my $fname;
          my $ftype = 0;
          my $fid   = 0;
          $xfer += $input->readStructBegin(\$fname);
          while (1) 
          {
            $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
            if ($ftype == TType::STOP) {
              last;
            }
            SWITCH: for($fid)
            {
              /^1$/ && do{              if ($ftype == TType::STRUCT) {
                $self->{document} = new thrift::adept::common::Document();
                $xfer += $self->{document}->read($input);
              } else {
                $xfer += $input->skip($ftype);
              }
              last; };
              /^2$/ && do{              if ($ftype == TType::STRUCT) {
                $self->{hltContentContainer} = new thrift::adept::common::HltContentContainer();
                $xfer += $self->{hltContentContainer}->read($input);
              } else {
                $xfer += $input->skip($ftype);
              }
              last; };
                $xfer += $input->skip($ftype);
            }
            $xfer += $input->readFieldEnd();
          }
          $xfer += $input->readStructEnd();
          return $xfer;
        }

sub write {
          my ($self, $output) = @_;
          my $xfer   = 0;
          $xfer += $output->writeStructBegin('DocumentProcessor_processAsync_args');
          if (defined $self->{document}) {
            $xfer += $output->writeFieldBegin('document', TType::STRUCT, 1);
            $xfer += $self->{document}->write($output);
            $xfer += $output->writeFieldEnd();
          }
          if (defined $self->{hltContentContainer}) {
            $xfer += $output->writeFieldBegin('hltContentContainer', TType::STRUCT, 2);
            $xfer += $self->{hltContentContainer}->write($output);
            $xfer += $output->writeFieldEnd();
          }
          $xfer += $output->writeFieldStop();
          $xfer += $output->writeStructEnd();
          return $xfer;
        }

package thrift::adept::module::DocumentProcessor_processAsync_result;
use base qw(Class::Accessor);
thrift::adept::module::DocumentProcessor_processAsync_result->mk_accessors( qw( success ) );

sub new {
          my $classname = shift;
          my $self      = {};
          my $vals      = shift || {};
          $self->{success} = undef;
          if (UNIVERSAL::isa($vals,'HASH')) {
            if (defined $vals->{success}) {
              $self->{success} = $vals->{success};
            }
          }
          return bless ($self, $classname);
}

sub getName {
          return 'DocumentProcessor_processAsync_result';
        }

sub read {
          my ($self, $input) = @_;
          my $xfer  = 0;
          my $fname;
          my $ftype = 0;
          my $fid   = 0;
          $xfer += $input->readStructBegin(\$fname);
          while (1) 
          {
            $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
            if ($ftype == TType::STOP) {
              last;
            }
            SWITCH: for($fid)
            {
              /^0$/ && do{              if ($ftype == TType::I64) {
                $xfer += $input->readI64(\$self->{success});
              } else {
                $xfer += $input->skip($ftype);
              }
              last; };
                $xfer += $input->skip($ftype);
            }
            $xfer += $input->readFieldEnd();
          }
          $xfer += $input->readStructEnd();
          return $xfer;
        }

sub write {
          my ($self, $output) = @_;
          my $xfer   = 0;
          $xfer += $output->writeStructBegin('DocumentProcessor_processAsync_result');
          if (defined $self->{success}) {
            $xfer += $output->writeFieldBegin('success', TType::I64, 0);
            $xfer += $output->writeI64($self->{success});
            $xfer += $output->writeFieldEnd();
          }
          $xfer += $output->writeFieldStop();
          $xfer += $output->writeStructEnd();
          return $xfer;
        }

package thrift::adept::module::DocumentProcessor_tryGetResult_args;
use base qw(Class::Accessor);
thrift::adept::module::DocumentProcessor_tryGetResult_args->mk_accessors( qw( requestId hltContentContainer ) );

sub new {
          my $classname = shift;
          my $self      = {};
          my $vals      = shift || {};
          $self->{requestId} = undef;
          $self->{hltContentContainer} = undef;
          if (UNIVERSAL::isa($vals,'HASH')) {
            if (defined $vals->{requestId}) {
              $self->{requestId} = $vals->{requestId};
            }
            if (defined $vals->{hltContentContainer}) {
              $self->{hltContentContainer} = $vals->{hltContentContainer};
            }
          }
          return bless ($self, $classname);
}

sub getName {
          return 'DocumentProcessor_tryGetResult_args';
        }

sub read {
          my ($self, $input) = @_;
          my $xfer  = 0;
          my $fname;
          my $ftype = 0;
          my $fid   = 0;
          $xfer += $input->readStructBegin(\$fname);
          while (1) 
          {
            $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
            if ($ftype == TType::STOP) {
              last;
            }
            SWITCH: for($fid)
            {
              /^1$/ && do{              if ($ftype == TType::I64) {
                $xfer += $input->readI64(\$self->{requestId});
              } else {
                $xfer += $input->skip($ftype);
              }
              last; };
              /^2$/ && do{              if ($ftype == TType::STRUCT) {
                $self->{hltContentContainer} = new thrift::adept::common::HltContentContainer();
                $xfer += $self->{hltContentContainer}->read($input);
              } else {
                $xfer += $input->skip($ftype);
              }
              last; };
                $xfer += $input->skip($ftype);
            }
            $xfer += $input->readFieldEnd();
          }
          $xfer += $input->readStructEnd();
          return $xfer;
        }

sub write {
          my ($self, $output) = @_;
          my $xfer   = 0;
          $xfer += $output->writeStructBegin('DocumentProcessor_tryGetResult_args');
          if (defined $self->{requestId}) {
            $xfer += $output->writeFieldBegin('requestId', TType::I64, 1);
            $xfer += $output->writeI64($self->{requestId});
            $xfer += $output->writeFieldEnd();
          }
          if (defined $self->{hltContentContainer}) {
            $xfer += $output->writeFieldBegin('hltContentContainer', TType::STRUCT, 2);
            $xfer += $self->{hltContentContainer}->write($output);
            $xfer += $output->writeFieldEnd();
          }
          $xfer += $output->writeFieldStop();
          $xfer += $output->writeStructEnd();
          return $xfer;
        }

package thrift::adept::module::DocumentProcessor_tryGetResult_result;
use base qw(Class::Accessor);
thrift::adept::module::DocumentProcessor_tryGetResult_result->mk_accessors( qw( success ) );

sub new {
          my $classname = shift;
          my $self      = {};
          my $vals      = shift || {};
          $self->{success} = undef;
          if (UNIVERSAL::isa($vals,'HASH')) {
            if (defined $vals->{success}) {
              $self->{success} = $vals->{success};
            }
          }
          return bless ($self, $classname);
}

sub getName {
          return 'DocumentProcessor_tryGetResult_result';
        }

sub read {
          my ($self, $input) = @_;
          my $xfer  = 0;
          my $fname;
          my $ftype = 0;
          my $fid   = 0;
          $xfer += $input->readStructBegin(\$fname);
          while (1) 
          {
            $xfer += $input->readFieldBegin(\$fname, \$ftype, \$fid);
            if ($ftype == TType::STOP) {
              last;
            }
            SWITCH: for($fid)
            {
              /^0$/ && do{              if ($ftype == TType::BOOL) {
                $xfer += $input->readBool(\$self->{success});
              } else {
                $xfer += $input->skip($ftype);
              }
              last; };
                $xfer += $input->skip($ftype);
            }
            $xfer += $input->readFieldEnd();
          }
          $xfer += $input->readStructEnd();
          return $xfer;
        }

sub write {
          my ($self, $output) = @_;
          my $xfer   = 0;
          $xfer += $output->writeStructBegin('DocumentProcessor_tryGetResult_result');
          if (defined $self->{success}) {
            $xfer += $output->writeFieldBegin('success', TType::BOOL, 0);
            $xfer += $output->writeBool($self->{success});
            $xfer += $output->writeFieldEnd();
          }
          $xfer += $output->writeFieldStop();
          $xfer += $output->writeStructEnd();
          return $xfer;
        }

package thrift::adept::module::DocumentProcessorIf;

use strict;


sub process{
  my $self = shift;
  my $document = shift;
  my $hltContentContainer = shift;

  die 'implement interface';
}

sub processAsync{
  my $self = shift;
  my $document = shift;
  my $hltContentContainer = shift;

  die 'implement interface';
}

sub tryGetResult{
  my $self = shift;
  my $requestId = shift;
  my $hltContentContainer = shift;

  die 'implement interface';
}

package thrift::adept::module::DocumentProcessorRest;

use strict;


sub new {
          my ($classname, $impl) = @_;
          my $self     ={ impl => $impl };

          return bless($self,$classname);
}

sub process{
          my ($self, $request) = @_;

          my $document = ($request->{'document'}) ? $request->{'document'} : undef;
          my $hltContentContainer = ($request->{'hltContentContainer'}) ? $request->{'hltContentContainer'} : undef;
          return $self->{impl}->process($document, $hltContentContainer);
        }

sub processAsync{
          my ($self, $request) = @_;

          my $document = ($request->{'document'}) ? $request->{'document'} : undef;
          my $hltContentContainer = ($request->{'hltContentContainer'}) ? $request->{'hltContentContainer'} : undef;
          return $self->{impl}->processAsync($document, $hltContentContainer);
        }

sub tryGetResult{
          my ($self, $request) = @_;

          my $requestId = ($request->{'requestId'}) ? $request->{'requestId'} : undef;
          my $hltContentContainer = ($request->{'hltContentContainer'}) ? $request->{'hltContentContainer'} : undef;
          return $self->{impl}->tryGetResult($requestId, $hltContentContainer);
        }

package thrift::adept::module::DocumentProcessorClient;


use base qw(thrift::adept::module::DocumentProcessorIf);
sub new {
          my ($classname, $input, $output) = @_;
          my $self      = {};
          $self->{input}  = $input;
          $self->{output} = defined $output ? $output : $input;
          $self->{seqid}  = 0;
          return bless($self,$classname);
}

sub process{
  my $self = shift;
  my $document = shift;
  my $hltContentContainer = shift;

                    $self->send_process($document, $hltContentContainer);
          return $self->recv_process();
}

sub send_process{
  my $self = shift;
  my $document = shift;
  my $hltContentContainer = shift;

          $self->{output}->writeMessageBegin('process', TMessageType::CALL, $self->{seqid});
          my $args = new thrift::adept::module::DocumentProcessor_process_args();
          $args->{document} = $document;
          $args->{hltContentContainer} = $hltContentContainer;
          $args->write($self->{output});
          $self->{output}->writeMessageEnd();
          $self->{output}->getTransport()->flush();
}

sub recv_process{
  my $self = shift;

          my $rseqid = 0;
          my $fname;
          my $mtype = 0;

          $self->{input}->readMessageBegin(\$fname, \$mtype, \$rseqid);
          if ($mtype == TMessageType::EXCEPTION) {
            my $x = new TApplicationException();
            $x->read($self->{input});
            $self->{input}->readMessageEnd();
            die $x;
          }
          my $result = new thrift::adept::module::DocumentProcessor_process_result();
          $result->read($self->{input});
          $self->{input}->readMessageEnd();

          if (defined $result->{success} ) {
            return $result->{success};
          }
          die "process failed: unknown result";
}
sub processAsync{
  my $self = shift;
  my $document = shift;
  my $hltContentContainer = shift;

                    $self->send_processAsync($document, $hltContentContainer);
          return $self->recv_processAsync();
}

sub send_processAsync{
  my $self = shift;
  my $document = shift;
  my $hltContentContainer = shift;

          $self->{output}->writeMessageBegin('processAsync', TMessageType::CALL, $self->{seqid});
          my $args = new thrift::adept::module::DocumentProcessor_processAsync_args();
          $args->{document} = $document;
          $args->{hltContentContainer} = $hltContentContainer;
          $args->write($self->{output});
          $self->{output}->writeMessageEnd();
          $self->{output}->getTransport()->flush();
}

sub recv_processAsync{
  my $self = shift;

          my $rseqid = 0;
          my $fname;
          my $mtype = 0;

          $self->{input}->readMessageBegin(\$fname, \$mtype, \$rseqid);
          if ($mtype == TMessageType::EXCEPTION) {
            my $x = new TApplicationException();
            $x->read($self->{input});
            $self->{input}->readMessageEnd();
            die $x;
          }
          my $result = new thrift::adept::module::DocumentProcessor_processAsync_result();
          $result->read($self->{input});
          $self->{input}->readMessageEnd();

          if (defined $result->{success} ) {
            return $result->{success};
          }
          die "processAsync failed: unknown result";
}
sub tryGetResult{
  my $self = shift;
  my $requestId = shift;
  my $hltContentContainer = shift;

                    $self->send_tryGetResult($requestId, $hltContentContainer);
          return $self->recv_tryGetResult();
}

sub send_tryGetResult{
  my $self = shift;
  my $requestId = shift;
  my $hltContentContainer = shift;

          $self->{output}->writeMessageBegin('tryGetResult', TMessageType::CALL, $self->{seqid});
          my $args = new thrift::adept::module::DocumentProcessor_tryGetResult_args();
          $args->{requestId} = $requestId;
          $args->{hltContentContainer} = $hltContentContainer;
          $args->write($self->{output});
          $self->{output}->writeMessageEnd();
          $self->{output}->getTransport()->flush();
}

sub recv_tryGetResult{
  my $self = shift;

          my $rseqid = 0;
          my $fname;
          my $mtype = 0;

          $self->{input}->readMessageBegin(\$fname, \$mtype, \$rseqid);
          if ($mtype == TMessageType::EXCEPTION) {
            my $x = new TApplicationException();
            $x->read($self->{input});
            $self->{input}->readMessageEnd();
            die $x;
          }
          my $result = new thrift::adept::module::DocumentProcessor_tryGetResult_result();
          $result->read($self->{input});
          $self->{input}->readMessageEnd();

          if (defined $result->{success} ) {
            return $result->{success};
          }
          die "tryGetResult failed: unknown result";
}
package thrift::adept::module::DocumentProcessorProcessor;

use strict;


sub new {
            my ($classname, $handler) = @_;
            my $self      = {};
            $self->{handler} = $handler;
            return bless ($self, $classname);
}

sub process {
            my ($self, $input, $output) = @_;
            my $rseqid = 0;
            my $fname  = undef;
            my $mtype  = 0;

            $input->readMessageBegin(\$fname, \$mtype, \$rseqid);
            my $methodname = 'process_'.$fname;
            if (!$self->can($methodname)) {
              $input->skip(TType::STRUCT);
              $input->readMessageEnd();
              my $x = new TApplicationException('Function '.$fname.' not implemented.', TApplicationException::UNKNOWN_METHOD);
              $output->writeMessageBegin($fname, TMessageType::EXCEPTION, $rseqid);
              $x->write($output);
              $output->writeMessageEnd();
              $output->getTransport()->flush();
              return;
            }
            $self->$methodname($rseqid, $input, $output);
            return 1;
}

sub process_process {
            my ($self, $seqid, $input, $output) = @_;
            my $args = new thrift::adept::module::DocumentProcessor_process_args();
            $args->read($input);
            $input->readMessageEnd();
            my $result = new thrift::adept::module::DocumentProcessor_process_result();
            $result->{success} = $self->{handler}->process($args->document, $args->hltContentContainer);
            $output->writeMessageBegin('process', TMessageType::REPLY, $seqid);
            $result->write($output);
            $output->writeMessageEnd();
            $output->getTransport()->flush();
}

sub process_processAsync {
            my ($self, $seqid, $input, $output) = @_;
            my $args = new thrift::adept::module::DocumentProcessor_processAsync_args();
            $args->read($input);
            $input->readMessageEnd();
            my $result = new thrift::adept::module::DocumentProcessor_processAsync_result();
            $result->{success} = $self->{handler}->processAsync($args->document, $args->hltContentContainer);
            $output->writeMessageBegin('processAsync', TMessageType::REPLY, $seqid);
            $result->write($output);
            $output->writeMessageEnd();
            $output->getTransport()->flush();
}

sub process_tryGetResult {
            my ($self, $seqid, $input, $output) = @_;
            my $args = new thrift::adept::module::DocumentProcessor_tryGetResult_args();
            $args->read($input);
            $input->readMessageEnd();
            my $result = new thrift::adept::module::DocumentProcessor_tryGetResult_result();
            $result->{success} = $self->{handler}->tryGetResult($args->requestId, $args->hltContentContainer);
            $output->writeMessageBegin('tryGetResult', TMessageType::REPLY, $seqid);
            $result->write($output);
            $output->writeMessageEnd();
            $output->getTransport()->flush();
}

1;