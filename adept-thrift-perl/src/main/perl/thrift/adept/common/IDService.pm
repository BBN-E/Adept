#
# Autogenerated by Thrift Compiler (0.9.0)
#
# DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
#
require 5.6.0;
use strict;
use warnings;
use Thrift;

use thrift::adept::common::Types;

# HELPER FUNCTIONS AND STRUCTURES

package thrift::adept::common::IDService_getId_args;
use base qw(Class::Accessor);

sub new {
  my $classname = shift;
  my $self      = {};
  my $vals      = shift || {};
  return bless ($self, $classname);
}

sub getName {
  return 'IDService_getId_args';
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
  $xfer += $output->writeStructBegin('IDService_getId_args');
  $xfer += $output->writeFieldStop();
  $xfer += $output->writeStructEnd();
  return $xfer;
}

package thrift::adept::common::IDService_getId_result;
use base qw(Class::Accessor);
thrift::adept::common::IDService_getId_result->mk_accessors( qw( success ) );

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
  return 'IDService_getId_result';
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
      /^0$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{success});
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
  $xfer += $output->writeStructBegin('IDService_getId_result');
  if (defined $self->{success}) {
    $xfer += $output->writeFieldBegin('success', TType::STRING, 0);
    $xfer += $output->writeString($self->{success});
    $xfer += $output->writeFieldEnd();
  }
  $xfer += $output->writeFieldStop();
  $xfer += $output->writeStructEnd();
  return $xfer;
}

package thrift::adept::common::IDService_getIdString_args;
use base qw(Class::Accessor);

sub new {
  my $classname = shift;
  my $self      = {};
  my $vals      = shift || {};
  return bless ($self, $classname);
}

sub getName {
  return 'IDService_getIdString_args';
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
  $xfer += $output->writeStructBegin('IDService_getIdString_args');
  $xfer += $output->writeFieldStop();
  $xfer += $output->writeStructEnd();
  return $xfer;
}

package thrift::adept::common::IDService_getIdString_result;
use base qw(Class::Accessor);
thrift::adept::common::IDService_getIdString_result->mk_accessors( qw( success ) );

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
  return 'IDService_getIdString_result';
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
      /^0$/ && do{      if ($ftype == TType::STRING) {
        $xfer += $input->readString(\$self->{success});
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
  $xfer += $output->writeStructBegin('IDService_getIdString_result');
  if (defined $self->{success}) {
    $xfer += $output->writeFieldBegin('success', TType::STRING, 0);
    $xfer += $output->writeString($self->{success});
    $xfer += $output->writeFieldEnd();
  }
  $xfer += $output->writeFieldStop();
  $xfer += $output->writeStructEnd();
  return $xfer;
}

package thrift::adept::common::IDServiceIf;

use strict;


sub getId{
  my $self = shift;

  die 'implement interface';
}

sub getIdString{
  my $self = shift;

  die 'implement interface';
}

package thrift::adept::common::IDServiceRest;

use strict;


sub new {
  my ($classname, $impl) = @_;
  my $self     ={ impl => $impl };

  return bless($self,$classname);
}

sub getId{
  my ($self, $request) = @_;

  return $self->{impl}->getId();
}

sub getIdString{
  my ($self, $request) = @_;

  return $self->{impl}->getIdString();
}

package thrift::adept::common::IDServiceClient;


use base qw(thrift::adept::common::IDServiceIf);
sub new {
  my ($classname, $input, $output) = @_;
  my $self      = {};
  $self->{input}  = $input;
  $self->{output} = defined $output ? $output : $input;
  $self->{seqid}  = 0;
  return bless($self,$classname);
}

sub getId{
  my $self = shift;

    $self->send_getId();
  return $self->recv_getId();
}

sub send_getId{
  my $self = shift;

  $self->{output}->writeMessageBegin('getId', TMessageType::CALL, $self->{seqid});
  my $args = new thrift::adept::common::IDService_getId_args();
  $args->write($self->{output});
  $self->{output}->writeMessageEnd();
  $self->{output}->getTransport()->flush();
}

sub recv_getId{
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
  my $result = new thrift::adept::common::IDService_getId_result();
  $result->read($self->{input});
  $self->{input}->readMessageEnd();

  if (defined $result->{success} ) {
    return $result->{success};
  }
  die "getId failed: unknown result";
}
sub getIdString{
  my $self = shift;

    $self->send_getIdString();
  return $self->recv_getIdString();
}

sub send_getIdString{
  my $self = shift;

  $self->{output}->writeMessageBegin('getIdString', TMessageType::CALL, $self->{seqid});
  my $args = new thrift::adept::common::IDService_getIdString_args();
  $args->write($self->{output});
  $self->{output}->writeMessageEnd();
  $self->{output}->getTransport()->flush();
}

sub recv_getIdString{
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
  my $result = new thrift::adept::common::IDService_getIdString_result();
  $result->read($self->{input});
  $self->{input}->readMessageEnd();

  if (defined $result->{success} ) {
    return $result->{success};
  }
  die "getIdString failed: unknown result";
}
package thrift::adept::common::IDServiceProcessor;

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

sub process_getId {
    my ($self, $seqid, $input, $output) = @_;
    my $args = new thrift::adept::common::IDService_getId_args();
    $args->read($input);
    $input->readMessageEnd();
    my $result = new thrift::adept::common::IDService_getId_result();
    $result->{success} = $self->{handler}->getId();
    $output->writeMessageBegin('getId', TMessageType::REPLY, $seqid);
    $result->write($output);
    $output->writeMessageEnd();
    $output->getTransport()->flush();
}

sub process_getIdString {
    my ($self, $seqid, $input, $output) = @_;
    my $args = new thrift::adept::common::IDService_getIdString_args();
    $args->read($input);
    $input->readMessageEnd();
    my $result = new thrift::adept::common::IDService_getIdString_result();
    $result->{success} = $self->{handler}->getIdString();
    $output->writeMessageBegin('getIdString', TMessageType::REPLY, $seqid);
    $result->write($output);
    $output->writeMessageEnd();
    $output->getTransport()->flush();
}

1;